import { useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { strategyExplanation } from '../strategyGlossary';

interface StrategyNameProps {
  name: string;
  placement?: 'top' | 'bottom';
}

export default function StrategyName({ name, placement = 'bottom' }: StrategyNameProps) {
  const anchorRef = useRef<HTMLSpanElement | null>(null);
  const [open, setOpen] = useState(false);
  const [position, setPosition] = useState({ left: 0, top: 0 });

  const updatePosition = () => {
    const rect = anchorRef.current?.getBoundingClientRect();
    if (!rect) {
      return;
    }
    setPosition({
      left: Math.max(12, Math.min(rect.left, window.innerWidth - 360)),
      top: placement === 'top' ? rect.top - 8 : rect.bottom + 8
    });
  };

  useEffect(() => {
    if (!open) {
      return;
    }
    updatePosition();
    const onViewportChange = () => updatePosition();
    window.addEventListener('resize', onViewportChange);
    window.addEventListener('scroll', onViewportChange, true);
    return () => {
      window.removeEventListener('resize', onViewportChange);
      window.removeEventListener('scroll', onViewportChange, true);
    };
  }, [open, placement]);

  return (
    <>
      <span
        ref={anchorRef}
      className={`strategyTooltip strategyTooltip-${placement}`}
      tabIndex={0}
      aria-label={`${name}: ${strategyExplanation(name)}`}
      onMouseEnter={() => setOpen(true)}
      onMouseLeave={() => setOpen(false)}
      onFocus={() => setOpen(true)}
      onBlur={() => setOpen(false)}
    >
      <span className="strategyLabel">{name}</span>
      <span className="infoDot" aria-hidden="true">
        i
      </span>
      </span>
      {open &&
        createPortal(
          <span
            className={`strategyPopover strategyPopoverFloating strategyPopover-${placement}`}
            role="tooltip"
            style={{ left: `${position.left}px`, top: `${position.top}px` }}
          >
            {strategyExplanation(name)}
          </span>,
          document.body
        )}
    </>
  );
}
